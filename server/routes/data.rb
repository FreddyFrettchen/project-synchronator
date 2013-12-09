#
# This file handles the data exchange with the
# android app.
#
class ServerHandler < Sinatra::Base
    namespace '/data' do
        # None of this routes is reachable without
        # user verification!
        before do
            content_type 'json'
            email, password = @params["email"], @params["password"]
            @user = User.authenticate(email, password)
            halt(403) unless @user
        end

        # Every request to the namespace "/add" has to contain
        # a parameter named "data" which holds the data to be
        # stored.
        namespace '/add' do
            ['calendar', 'contact', 'note'].each do |route|
                post "/#{route}" do
                    begin
                        @user.send("add_#{route}", :data => @params['data']) 
                    rescue Sequel::ValidationFailed => error
                        halt 304
                    end
                    halt 200
                end
            end
        end

        ['calendar', 'contacts', 'notes'].each do |route|
            # "/update" requests need an additional url parameter
            # which is the id of the object to be updated.
            # New data has to be provided by the field "data".
            namespace '/update' do
                post "/#{route}/:id" do
                    changed = @user.send("#{route}_dataset")
                    .where(:id => @params[:id])
                    .update(:data => @params['data'])
                    halt(404) if changed == 0
                    halt(200)
                end
            end

            # "/get" requests return the complete set of records
            # as json list to the client. No params needed.
            namespace '/get' do
                post "/#{route}" do
                    @user.send(route).map{|e| e.values}.to_json
                end
            end
        end
    end
end
