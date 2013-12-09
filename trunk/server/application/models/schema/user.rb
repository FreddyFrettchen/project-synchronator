DB.create_table? :user do
	primary_key :id
    String      :email
    String      :password

	DateTime 	:register
    TrueClass   :approved
end
