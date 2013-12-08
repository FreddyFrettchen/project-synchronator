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
	
	use Rack::Session::Pool, :expire_after => 2592000
	
    configure do
		set :base, File.dirname(__FILE__)
	end
	
	helpers do
		include Rack::Utils
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
