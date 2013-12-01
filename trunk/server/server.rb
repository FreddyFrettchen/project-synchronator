#!/usr/bin/ruby

require 'pp'
require 'json'
require 'bundler'
Bundler.require

# spin up connection to database
require_relative 'db'

class ServerHandler < Sinatra::Base
	register Sinatra::Namespace
	register Sinatra::ConfigFile
	
	config_file './config.yml'
	
	configure do
		set :base, File.dirname(__FILE__)
	end
	
	use Rack::Session::Pool, :expire_after => 2592000
	use Rack::Logger

    # datatype of coice

	helpers do
		include Rack::Utils
		
		def logger
			request.logger
		end
	end
	
    # reload files between requests
	configure :development do
		register Sinatra::Reloader
	end
	
	configure :production do
	end
end

require_relative 'routes/init'
require_relative 'models/init'
