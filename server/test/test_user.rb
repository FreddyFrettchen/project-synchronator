require 'pp'
require 'test/unit'

DB = Sequel.sqlite(':memory:')

require_relative '../models/schema/db.rb'
require_relative '../models/user.rb'
require_relative '../models/calendar.rb'
require_relative '../models/contacts.rb'
require_relative '../models/notes.rb'

class TestUser < Test::Unit::TestCase
    def test_register_success
        email, pw = "email@addr.de", "123"
        user = User.register(email, pw)
        assert_equal email, user.email
        assert_equal pw, user.password
        assert_equal false, user.approved
    end

    def test_register_duplicate_email
        email, pw = "duplicate@addr.de", "123"
        User.register(email, pw)
        user = User.register(email, pw)
        assert_equal false, user
    end

    def test_authenticate_not_approved
        email, pw = "user@addr.de", "123"
        User.register(email, pw)
        assert_equal false, User.authenticate( email, pw )
    end

    def test_authenticate_approved
        email, pw = "userapprove@addr.de", "123"
        User.register(email, pw).approve!
        assert_not_equal false, User.authenticate( email, pw )
    end

    def test_approve!
        email, pw = "approve@addr.de", "123"
        user = User.register(email, pw)
        user.approve!
        user.reload
        assert_equal true, user.approved
    end

    def test_reject!
        email, pw = "reject@addr.de", "123"
        user = User.register(email, pw)
        user.reject!
        find = User.find(:id => user.id)
        assert_equal nil, find
    end

    def test_get_nonexistent_data
        email, pw = "get@addr.de", "123"
        user = User.register(email, pw)
        user.approve!
        assert_equal "", user.contacts_data
        assert_equal "", user.calendar_data
        assert_equal "", user.notes_data
    end

    def test_set_get_data
        email, pw = "set@addr.de", "123"
        user = User.register(email, pw)
        user.approve!

        cal, con, note = "CAL", "CON", "NOTE"
        assert_equal true, user.set_calendar_data!(cal)
        assert_equal true, user.set_contacts_data!(con)
        assert_equal true, user.set_notes_data!(note)

        assert_equal cal, user.calendar_data
        assert_equal con, user.contacts_data
        assert_equal note, user.notes_data
    end
end
