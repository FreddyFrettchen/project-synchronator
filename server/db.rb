DB = Sequel.connect(ENV['DATABASE_URL'] || 'sqlite://data/database.sqlite')
