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

        post '/authenticate' do
            success = User.authenticate( @email, @password )
            halt( success ? 200 : 403 )
        end
        
        post '/register' do
            success = User.register( @email, @password )
            halt( success ? 202 : 403 )
        end
    end
end
