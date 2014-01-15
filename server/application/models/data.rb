['Calendar', 'Contacts', 'Notes'].each do |model|
    table = Sequel::Model(model.downcase.to_sym)
    Object.const_set(model, Class.new(table){
        plugin :validation_helpers

        #@override
        def validate
            super
            validates_presence :data
        end

        #@override
        def before_update
            self.last_update = Time.new
        end

        #@override
        def before_save
            self.last_update = Time.new
        end

        # Returns a hash containing all fields,
        # that should be send to the client.
        def public_values
            { 'id' => self.id, 'data' => self.data, 
                'id_data' => self.id_data, 'deleted' => self.deleted }
        end
    })
end
