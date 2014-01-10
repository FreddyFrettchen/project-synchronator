require_relative '../server'

class TestUserRoute < Test::Unit::TestCase
  include Rack::Test::Methods

  def app
    ServerHandler
  end

  def test_register
    credentials = {"email" => "reg@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    assert_equal 200, last_response.status
  end
  
  def test_register_dup
    credentials = {"email" => "regdup@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    assert_equal 200, last_response.status
    post '/user/register', credentials
    assert_equal 403, last_response.status
  end

  def test_authenticate
    credentials = {"email" => "login@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    # user has to be approved
    post '/user/authenticate', credentials
    assert_equal 403, last_response.status

    User.find(:email => credentials["email"]).approve!
    post '/user/authenticate', credentials
    assert_equal 200, last_response.status
  end
  
  def test_delete
    credentials = {"email" => "delete@mail.com", "password" => "dpd"}
    post '/user/register', credentials
    # user has to be approved
    post '/user/authenticate', credentials
    assert_equal 403, last_response.status

    User.find(:email => credentials["email"]).approve!
    post '/user/authenticate', credentials
    assert_equal 200, last_response.status
  end
end
