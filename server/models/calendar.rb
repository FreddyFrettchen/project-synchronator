class Calendar < Sequel::Model(:calendars)
    plugin :validation_helpers
    include DataCommon
end
