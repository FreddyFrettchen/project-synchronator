class Notes < Sequel::Model(:notes)
    def before_update
        self.last_sync = Time.new
    end
end
