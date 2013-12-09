DB.create_table? :calendar do
	primary_key :id
    Integer     :id_user
    Text        :data
	DateTime 	:last_sync
end
