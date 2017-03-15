# Flow 

## Initialization

	- Assign player ids []
	- Send board config [] 
	  
	
## First Turn

``` 
	// game rules say youngest to oldest,
	// we'll go by server's decision
	
	// first round is in order 
	for player in players:
		server -> announce players turn
		player -> choose settlement
		player -> choose road
	
	// then we go backwards in reverse
	for player in players.reverse():
		server -> announce players turn
		player -> choose settlement
		player -> choose road	
	
	// only for second settlement
	gather_starting_resources() 
	
	server -> announce game start
```
	
## Other Turns

 - game consists of multiple iterations of this loop
 - server announces victory point changes
 - no explicit end of loop

```
	loop:
		server -> announce turn start
		for player in players:
			server -> announce player turn
	
            [player -> play dev card]

			server -> announce dice roll
		
			if roll is 7:
				handle_roll(7)

			while not done <- player:
				player -> perform_action()
```

Event handler for dice roll, should trigger robber action

```
	fn handle_roll(int n):
        // TODO: define roll event handler
```


Main phase of turn consists of many actions being performed by the player

```
	fn perform_action():
        // TODO: define main turn phase
```


# TODO

## General

	- Server Announcements []
	- Move to protobuf ver 3.0 []
	- Setup []
	- Timing  []

### Requests

	- Purchase settlement, road etc. []
	- Playing development card []
	- Trade offer []

### Responses
	
	- Need more than success/fail? []



