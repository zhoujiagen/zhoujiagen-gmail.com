package example

# Policy
# 
# 1. Servers reachable from the Internet must not expose the insecure 'http' protocol.
# 2. Servers are not allowed to expose the 'telnet' protocol.

default allow = false

allow = true {
    count(violation) == 0
}

violation[server.id] {
    some server
    public_server[server]
    server.protocols[_] == "http"
}

violation[server.id] {
    server := input.servers[_]
    server.protocols[_] = "telnet"
}

public_server[server] {
    some i, j
    server := input.servers[_]
    server.ports[_] == input.ports[i].id
    input.ports[i].network == input.networks[j].id
    input.networks[j].public
}