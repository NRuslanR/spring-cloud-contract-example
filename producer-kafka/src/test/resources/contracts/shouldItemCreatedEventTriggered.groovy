import org.springframework.cloud.contract.spec.Contract

Contract.make 
{
    description "should 'ItemCreated' event triggered when item was created"

    input
    {
        triggeredBy('createItem()')
    }

    label('triggerItemCreatedEvent')

    outputMessage
    {
        sentTo('ItemCreated')
        body([
            id: $(uuid()),
            name: $(nonBlank()),
            createdAt: $(isoDateTime())
        ])
    }
}