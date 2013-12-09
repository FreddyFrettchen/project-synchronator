class Calendar < Sequel::Model(:calendars)
    plugin :validation_helpers

    def validate
        super
        validates_presence :data
    end

    def before_update
        self.last_sync = Time.new
    end

    def before_save
        self.last_sync = Time.new
    end
end
