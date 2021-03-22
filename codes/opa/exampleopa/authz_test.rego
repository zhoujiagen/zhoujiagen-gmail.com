package authz

# opa test auth*.rego -v
# opa test --format=json authz*.rego 
# opa test --coverage --format=json authz*.rego
# opa test --bench --format=gobench authz*.rego 

# data mocking
input1 = {"path": ["users"], "method": "POST"}
input2 = {"path": ["users"], "method": "GET"}
input3 = {"path": ["users", "bob"], "method": "GET", "user_id": "bob"}
input4 = {"path": ["users", "bob"], "method": "GET", "user_id": "alice"}

test_post_allowed {
    allow with input as input1
}

test_get_anonymous_denied {
    not allow with input as input2
}

test_get_user_allowed {
    allow with input as input3
}

test_get_another_user_denied {
    not allow with input as input4
}