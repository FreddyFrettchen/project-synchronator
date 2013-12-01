DB.create_table? :contacts do
	primary_key :id
    Integer     :id_user
    Text        :data
	DateTime 	:last_sync
end
