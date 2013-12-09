#!/usr/bin/ruby

require 'pp'
require 'json'
require 'bundler'

# switch so tests can run without
# warnings and possible race conditions.
unless ENV['TEST']
    Bundler.require

    # spin up connection to database
    require_relative 'db'
end 

class ServerHandler < Sinatra::Base
    register Sinatra::Namespace
    register Sinatra::ConfigFile

    configure do
        set :base, File.dirname(__FILE__)
        set :logging, true
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
