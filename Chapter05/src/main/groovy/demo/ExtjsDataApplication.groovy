package demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class ExtjsDataApplication {

    static void main(String[] args) {
        SpringApplication.run ExtjsDataApplication, args
    }
    
    final static BOOKS = [    
        "Zend Framework",
        "Beginning F#",
        "Pro Hadoop"
    ]
    
    @RequestMapping(value="/books", method=RequestMethod.GET)
    def books() {
        [ success: true, message: "Loaded data", data: BOOKS ]
    }

    final static KEYS = [ 'first', 'last', 'email' ] as Set
    final static ALL_KEYS = KEYS + [ 'id' ] as Set  
    
    final static DEFAULT_USERS = [
        [ first: "Fred",    last: "Flintstone", email: "fred@flintstone.com"    ],
        [ first: "Wilma",   last: "Flintstone", email: "wilma@flintstone.com"   ],
        [ first: "Pebbles", last: "Flintstone", email: "pebbles@flintstone.com" ],
        [ first: "Barney",  last: "Rubble",     email: "barney@rubble.com"      ],
        [ first: "Betty",   last: "Rubble",     email: "betty@rubble.com"       ],
        [ first: "BamBam",  last: "Rubble",     email: "bambam@rubble.com"      ],
    ] 
    
    final Map users = [:]
    
    ExtjsDataApplication() {
        DEFAULT_USERS.eachWithIndex { value, index->
            def id = (1 + index)
            def newUser = [ id: id ]
            newUser << value
            users[id] = newUser
        }
    }
    
    @RequestMapping(value="/restful/users", method=RequestMethod.GET)
    def listUsers() {
        [ success: true, message: "Loaded data", data: users.values() ]
    }
    
    @RequestMapping(value="/restful/users", method=RequestMethod.POST)
    def addUser(@RequestBody userObject) {
        
        if (userObject instanceof Map) {
            def id = (1 + users.size())
            def newUser = [ id: id ]
            newUser << userObject.subMap(KEYS)

            if (newUser.keySet() == ALL_KEYS) {
                users[id] = newUser
                [ success: true, message: "User added", data: newUser ]
            } else {
                [ success: false, message: "Invalid user object - missing elements" ]
            }
        } else {
            [ success: false, message: "Invalid user object" ]
        }
    }

    @RequestMapping(value="/restful/users/{id}", method=RequestMethod.DELETE)
    def deleteUser(@PathVariable("id") int id) {
        
        if (users.containsKey(id)) {
            def deleted = users.remove(id)
            [ success: true, message: "User deleted", data: deleted ]
        } else {
            [ success: false, message: "Unknown user: [$id]" ]
        }
    }
    
    @RequestMapping(value="/restful/users/{id}", method=RequestMethod.PUT)
    def updateUser(@PathVariable("id") int id, @RequestBody userObject) {
        
        if (users.containsKey(id) && userObject instanceof Map) {
            def updated = users.get(id)
            updated.each { key, value->
                if (userObject.containsKey(key)) {
                    updated[key] = userObject[key]
                }
            }
            
            [ success: true, message: "User updated", data: updated ]
        } else {
            [ success: false, message: "Unknown user: [$id]" ]
        }
    }

}
