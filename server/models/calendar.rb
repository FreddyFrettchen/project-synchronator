class Calendar < Sequel::Model(:calendars)
    def before_update
        self.last_sync = Time.new
    end

    def before_create
        self.last_sync = Time.new
    end
end
