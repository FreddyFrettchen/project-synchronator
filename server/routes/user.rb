class ServerHandler < Sinatra::Base
    namespace '/user' do
        before do
            content_type 'text/plain'
            @email, @password = @params["email"], @params["password"]
        end

        post '/authenticate' do
            success = User.authenticate( @email, @password )
            code = if success then "OK" else "ERROR" end
            { "code" => code }.to_json
        end
        
        post '/register' do
            success = User.register( @email, @password )
            code = if success then "OK" else "ERROR" end
            { "code" => code }.to_json
        end
    end
end
