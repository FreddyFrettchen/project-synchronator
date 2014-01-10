#
# Handles user registration and authentication.
# Each request requires the post parameters 
# email and password.
#
# If not all fields are passed, the statuscode 
# 400 for Bad Request is send.
#
class ServerHandler < Sinatra::Base
    namespace '/user' do
        before do
            content_type 'text/plain'
            @email, @password = @params["email"], @params["password"]
        end

        %w{authenticate register}.each do |route|
            post "/#{route}" do
                success = User.send(route,  @email, @password )
                halt( success ? 200 : 403 )
            end
        end

        post "/delete" do
           user = User.authenticate(@email, @password)
           halt(500) unless user
           print user.destroy
        end
    end
end
