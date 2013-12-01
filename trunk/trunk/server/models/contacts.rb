class Contacts < Sequel::Model(:contacts)
    def before_update
        self.last_sync = Time.new
    end
end
