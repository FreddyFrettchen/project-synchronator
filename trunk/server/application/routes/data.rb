#
# This file handles the data exchange with the
# android app.
#
class ServerHandler < Sinatra::Base

    PLURAL_ROUTES   = %w{calendar contacts notes}
    SINGULAR_ROUTES = %w{calendar contact note}

    namespace '/data' do
        # None of this routes is reachable without
        # user verification!
        before do
            email, password = @params["email"], @params["password"]
            @user = User.authenticate(email, password)
            halt(403) unless @user
        end

        # Every request to the namespace "/add" has to contain
        # a parameter named "data" which holds the data to be
        # stored.
        namespace '/add' do
            SINGULAR_ROUTES.each do |route|
                post "/#{route}" do
                    begin
                        @user.send("add_#{route}", 
                                   :data => @params['data']) 
                    rescue Sequel::ValidationFailed => error
                        halt 304
                    end
                    halt 200
                end
            end
        end

        PLURAL_ROUTES.each do |route|

            before do
                content_type :json
            end

            # "/update" requests need an additional url parameter
            # which is the id of the object to be updated.
            # New data has to be provided by the field "data".
            namespace '/update' do
                post "/#{route}/:id" do
                    changed = @user.send("#{route}_dataset")
                    .where(:id => @params[:id])
                    .update(:data => @params['data'])
                    halt( changed.eql(1) ? 200 : 404 )
                end
            end
            
            namespace '/delete' do
                post "/#{route}/:id" do
                    changed = @user.send("#{route}_dataset")
                    .where(:id => @params[:id])
                    .delete
                    halt( (changed == 1) ? 200 : 404 )
                end
            end

            # "/get" requests return the complete set of records
            # as json list to the client. No params needed.
            namespace '/get' do
                post "/#{route}" do
                    @user.send(route).map(&:public_values).to_json
                end
            end

            # "/sync" expects the parameter "last_sync" to be
            # passed. I has to contain a timestamp of the last
            # successful syncronisation or refresh.
            # Every entry in the table that is newer than the
            # stamp, is send back to the user.
            namespace '/sync' do
                post "/#{route}" do
                    last_sync = @params['last_sync'].to_i
                    @user.send(route)
                    .select{|e| e.last_update.to_i > last_sync}
                    .map(&:public_values).to_json
                end
            end

            namespace '/ids' do
                post "/#{route}" do
                    @user.send(route)
                    .map{|e|e.id}.to_json
                end
            end
        end
    end
end
