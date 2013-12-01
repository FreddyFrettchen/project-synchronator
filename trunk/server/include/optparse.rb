require 'optparse'

@options = {
    :ssl => {
    :private_key_file => 'data/ssl/pubkey.key', 
    :cert_chain_file => 'data/ssl/cacert.crt', 
    :verify_peer => false
},
    :host => "127.0.0.1",
    :port => 45678
}

OptionParser.new do |opts|
    opts.banner = "Usage: server [options]"

    opts.on("-p", "--port PORT", "set port to listen on") do |p|
        @options[:port] = p
    end

    opts.on("-a", "--host HOST", "set host") do |h|
        @options[:host] = h
    end

    opts.on("-k","--ssl-private-key-file KEYFILE", 
            "set the private file to read the private key from") do |k|
        @options[:ssl][:private_key_file] = k
    end

    opts.on("-c", "--cert-chain-file CHAIN", "set file to read certificate chain from") do |c|
        @options[:ssl][:cert_chain_file] = c
    end
    
    opts.on("-v", "--verify-peer", "enable peer verification") do |v|
        @options[:ssl][:verify_peer] = v
    end
end.parse!
