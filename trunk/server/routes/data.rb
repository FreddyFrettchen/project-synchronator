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
        #
        # None of this routes is reachable without
        # user verification!
        #
        before do
            content_type 'json'
            email, password = @params["email"], @params["password"]
            @user = User.authenticate(email, password)
            halt(403) unless @user
        end

        namespace '/add' do
            before do
                @data = {
                    :id_user => @user.id,
                    :data => @params["data"]
                }
            end

            post '/calendar' do
                entry = Calendar.new(@data) 
                halt(403) unless entry.valid?
                entry.save
                halt(200)
            end

            post '/contacts' do
                entry = Contacts.new(@data) 
                halt(403) unless entry.valid?
                entry.save
                halt(200)
            end

            post '/notes' do
                entry = Notes.new(@data) 
                halt(403) unless entry.valid?
                entry.save
                halt(200)
            end
        end

        namespace '/get' do
            post '/calendar' do
                @user.calendar.map{|e| e.values}.to_json
            end

            post '/contacts' do
                @user.contacts.map{|e| e.values}.to_json
            end

            post '/notes' do
                @user.notes.map{|e| e.values}.to_json
            end
        end

        namespace '/update' do
            before do
                @data = {
                    :id_user => @user.id,
                    :data => @params["data"],
                    :last_sync => Time.now
                }
            end

            post '/calendar/:id' do
                DB[:calendars].update(@data)
            end

            post '/contacts/:id' do
                DB[:contacts].update(@data)
            end

            post '/notes/:id' do
                DB[:notes].update(@data)
            end
        end
    end
end
