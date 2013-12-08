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
