[:contacts, :calendar, :notes].each do |table|
    DB.create_table? table do
        primary_key :id
        Integer     :id_user
        # id of local storage for CREATE operation
        Integer     :id_data
        Text        :data
        DateTime 	:last_update
        TrueClass   :deleted
    end
end
