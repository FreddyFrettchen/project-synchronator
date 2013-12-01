class ServerHandler < Sinatra::Base
    namespace '/data' do
        before do
            content_type 'text/plain'
            email, password = @params["email"], @params["password"]
            @user = User.authenticate(email, password)
            halt(403) unless @user
        end

        namespace '/set' do
            post '/calendar' do
                @user.set_calendar_data! request['data']
            end

            post '/contacts' do
                @user.set_contacts_data! request['data']
            end

            post '/notes' do
                @user.set_notes_data! request['data']
            end
        end

        namespace '/get' do
            post '/calendar' do
                @user.calendar_data
            end

            post '/contacts' do
                @user.contacts_data
            end

            post '/notes' do
                @user.notes_data
            end
        end
    end
end
