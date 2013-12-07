class User < Sequel::Model(:user)
    plugin :validation_helpers

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

    def self.authenticate( email, password )
        login = User.find( :email => email, 
                          :password=> password,
                          :approved => true )
        login.nil?? false : login
    end

    def self.register( email, password )
        registration = self.new( :email => email,
                                :password => password,
                                :approved => false )
        registration.valid?? registration.save : false 
    end

    def approve!
        self.approved = true
        self.save
    end

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
