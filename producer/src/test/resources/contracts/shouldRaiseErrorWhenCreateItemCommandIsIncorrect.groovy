import org.springframework.cloud.contract.spec.Contract

Contract.make 
{
    description "should raise error when CreateItemCommand is incorrect"

    request 
    {
        method POST()
        
        url("/api/items")
        {

        }

        headers
        {
            contentType(applicationJson())
        }

        body(
            name: ""
        )
    }

    response
    {
        status 400

        headers
        {
            contentType(applicationJson())
        }

        body([
            error: [
                message: $(value(consumer("Item's name must not be empty")), producer(nonBlank()))
            ]
        ])

        bodyMatchers
        {
            jsonPath('$.error.message', byRegex(nonBlank()))
        }
    }
}