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
	
	use Rack::Session::Pool, :expire_after => 2592000 # we have no sessions. then why do we have this?
	
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

    # this doesnt really matter but better
    # than the default setting.
    # Almost all request only have head responses,
    # so no body will be send back
    before do
        content_type 'text/plain'
    end
end

require_relative 'routes/init'
require_relative 'models/init'
