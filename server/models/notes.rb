class Notes < Sequel::Model(:notes)
    plugin :validation_helpers
    include DataCommon
end
