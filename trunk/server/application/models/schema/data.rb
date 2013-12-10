[:contacts, :calendar, :notes].each do |table|
    DB.create_table? table do
        primary_key :id
        Integer     :id_user
        Text        :data
        DateTime 	:last_update
    end
end
