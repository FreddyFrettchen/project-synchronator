class User < Sequel::Model(:user)
    plugin :validation_helpers

    # relations to other tables
    one_to_many :notes,    :key => :id_user, :class => :Notes
    one_to_many :contacts, :key => :id_user, :class => :Contacts
    one_to_many :calendar, :key => :id_user, :class => :Calendar

    def validate
        super
        validates_unique :email
    end

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
                                :approved => false )
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

    def contacts_data
        self.contacts.data rescue "" 
    end

    def calendar_data
        self.calendar.data rescue ""
    end

    def notes_data
        self.notes.data rescue ""
    end

    def set_data!( model, data )
        row = model.find_or_create(:id_user => self.id)
        row.data = data
        row.save ? true : false 
    end

    def set_calendar_data!( data )
        set_data!( Calendar, data )
    end
    
    def set_contacts_data!( data )
        set_data!( Contacts, data )
    end
    
    def set_notes_data!( data )
        set_data!( Notes, data )
    end

    def to_s
        "<USER ID:#{self.id} MAIL:#{self.email} APPROVED:#{self.approved} REG:#{self.register}>" 
    end
end
