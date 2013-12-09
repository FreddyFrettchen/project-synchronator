#
# This file handles the data exchange with the
# android app.
# 
# Every request to the namespace "/add" has to contain
# a parameter named "data" which holds the data to be
# stored.
#
# "/get" requests return the complete set of records
# as json list to the client. No params needed.
#
# "/update" requests need an additional url parameter
# which is the id of the object to be updated.
# New data has to be provided by the field "data".
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

        namespace '/get' do
            ['calendar', 'contacts', 'notes'].each do |route|
                post "/#{route}" do
                    @user.send(route).map{|e| e.values}.to_json
                end
            end
        end

        namespace '/update' do
            before do
                @data = {
                    :id_user => @user.id,
                    :data => @params['data'],
                    :last_sync => Time.now
                }
            end

            ['calendar', 'contacts', 'notes'].each do |route|
                post "/#{route}/:id" do
                    entry = Calendar[@params[:id].to_i]
                    halt(404) if entry.nil?
                    entry.update(@data)
                    halt(200)
                end
            end
        end
    end
end
