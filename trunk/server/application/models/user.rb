class User < Sequel::Model(:user)
    plugin :validation_helpers

    # relations to other tables
    one_to_many :notes,    :key => :id_user, :class => :Notes
    one_to_many :contacts, :key => :id_user, :class => :Contacts
    one_to_many :calendar, :key => :id_user, :class => :Calendar

    #@override
    def validate
        super
        validates_presence [:email, :password]
        validates_format /@/, :email # change this?
        validates_unique :email
    end

    #@override
    def before_save
        super
        self.register ||= Time.new 
    end

    # check given login credentials.
    # returns the user if data matches
    # and false otherwise.
    def self.authenticate( email, password )
        login = User.find( :email => email, 
                          :password=> password,
                          :approved => true )
        login.nil?? false : login
    end

    # returns the new registred user or
    # false if the registration failed.
    # This can happen if the email is already in use.
    def self.register( email, password )
        registration = self.new( :email => email,
                                :password => password,
                                :approved => true ) # TODO this should be false for production
        registration.valid?? registration.save : false 
    end

    # a user has to be approved to use the service.
    def approve!
        self.approved = true
        self.save
    end

    # if a user should not be accepted, this
    # function deletes that user unless he is
    # already approved.
    def reject!
        return false if self.approved == true
        self.destroy
    end

    def delete_account
        self.contacts.each(&:destroy)
        self.calendar.each(&:destroy)
        self.notes.each(&:destroy)
        self.destroy
    end

    #@override
    def to_s
        "<USER ID:#{self.id} MAIL:#{self.email} APPROVED:#{self.approved} REG:#{self.register}>" 
    end
end
