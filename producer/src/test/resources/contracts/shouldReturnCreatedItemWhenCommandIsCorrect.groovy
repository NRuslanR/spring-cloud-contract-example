import org.springframework.cloud.contract.spec.Contract

Contract.make 
{
    description "should return created item when command is correct"

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
            name: "Item#1"
        )
    }

    response 
    {
        status 201

        headers 
        {
            contentType(applicationJson())
        }

        body(
            id: $(uuid()),
            name: "Item#1",
            createdAt: $(isoDateTime())
        )
    }
}