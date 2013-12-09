class Contacts < Sequel::Model(:contacts)
    plugin :validation_helpers
    include DataCommon
end
