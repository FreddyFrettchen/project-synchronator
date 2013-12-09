DB = Sequel.connect(ENV['DATABASE_URL'] || 'sqlite://application/data/database.sqlite')
