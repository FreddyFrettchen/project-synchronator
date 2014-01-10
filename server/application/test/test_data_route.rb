require_relative '../server'

class TestDataRoute < Test::Unit::TestCase
  include Rack::Test::Methods

  def app
    ServerHandler
  end

  def test_add_data
    credentials = {"email" => "add@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    User.find(:email => credentials["email"]).approve!

    # set some data to be set
    credentials["data"] = "TESTDATA"
    %w{calendar contact note}.each do |route|
        post "/data/add/#{route}", credentials
        assert_equal 200, last_response.status
    end
  end
  
  def test_add_data_fail
    credentials = {"email" => "addfail@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    User.find(:email => credentials["email"]).approve!

    # omg data is not set, this is invalid! :)
    %w{calendar contact note}.each do |route|
        post "/data/add/#{route}", credentials
        assert_equal 304 , last_response.status
    end
  end
  
  def test_add_data_fail_wrong_credentials
    credentials = {"email" => "addfailcred@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    User.find(:email => credentials["email"]).approve!
   
    # now data is set, but creds are wrong, oh noes.
    credentials["data"] = "TESTDATA"
    credentials["password"] = "thewrongone"
    %w{calendar contact note}.each do |route|
        post "/data/add/#{route}", credentials
        assert_equal 403 , last_response.status
    end
  end
  
  def test_get_data
    credentials = {"email" => "get@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    User.find(:email => credentials["email"]).approve!

    # set some data to be set
    credentials["data"] = "TESTDATA"
    %w{calendar contact note}.each do |route|
        post "/data/add/#{route}", credentials
    end

    # now get this data!
    %w{calendar contacts notes}.each do |route|
        post "/data/get/#{route}", credentials
        assert_equal 200 , last_response.status
        assert_equal credentials["data"], JSON.parse(last_response.body).first["data"]
    end
  end

  def test_update_data
    credentials = {"email" => "update@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    User.find(:email => credentials["email"]).approve!

    # set some data to be set
    credentials["data"] = "TESTDATA"
    %w{calendar contact note}.each do |route|
        post "/data/add/#{route}", credentials
    end

    # get the set data
    data = {}
    %w{calendar contacts notes}.each do |route|
        post "/data/get/#{route}", credentials
        data[route] = JSON.parse(last_response.body).first
    end

    credentials["data"] = "UPDATEDDATA"
    # now we update this data
    data.each_pair do |route,entry|
        credentials["data_id"] = entry["id"].to_s
        post "/data/update/#{route}", credentials
        assert_equal 200 , last_response.status
        post "/data/get/#{route}", credentials
        assert_equal credentials["data"], JSON.parse(last_response.body).first["data"]
    end

  end

  def test_sync_data

  end
end
