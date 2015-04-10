package demo

import java.util.concurrent.ConcurrentNavigableMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.atomic.AtomicInteger

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
    
    final ConcurrentNavigableMap<Integer, Map> usersStore = new ConcurrentSkipListMap<Integer, Map>()
    final AtomicInteger usersIndex = new AtomicInteger(1);

    ExtjsDataApplication() {
        DEFAULT_USERS.eachWithIndex { value, index->
            def id = usersIndex.getAndIncrement()
            def newUser = [ id: id ]
            newUser << value
            usersStore[id] = newUser
        }
    }
    
    @RequestMapping(value="/users", method=RequestMethod.GET)
    def getUsers() {
        [ success: true, message: "Loaded data", data: usersStore.values() ]
    }
    
    @RequestMapping(value="/users/{id}", method=RequestMethod.GET)
    def getUser(@PathVariable("id") int id) {
        if (usersStore.containsKey(id)) {
            def user = usersStore.get(id)
            [ success: true, message: "Loaded data", data: user ]
        } else {
            [ success: false, message: "Unknown user: [$id]" ]
        }
    }

    @RequestMapping(value="/users", method=RequestMethod.POST)
    def addUser(@RequestBody userObject) {
        
        if (userObject instanceof Map) {
            def id = usersIndex.getAndIncrement()
            def newUser = [ id: id ]
            newUser << userObject.subMap(KEYS)

            if (newUser.keySet() == ALL_KEYS) {
                usersStore[id] = newUser
                [ success: true, message: "User added", data: newUser ]
            } else {
                [ success: false, message: "Invalid user object - missing elements" ]
            }
        } else {
            [ success: false, message: "Invalid user object" ]
        }
    }

    @RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
    def deleteUser(@PathVariable("id") int id) {

        if (usersStore.containsKey(id)) {
            def deleted = usersStore.remove(id)
            [ success: true, message: "User deleted", data: deleted ]
        } else {
            [ success: false, message: "Unknown user: [$id]" ]
        }
    }

    @RequestMapping(value="/users", method=RequestMethod.PUT)
    def updateUser(@RequestBody userObject) {

        if (userObject instanceof Map && userObject.containsKey('id')) {
            updateUser(userObject.id, userObject)
        } else {
            [ success: false, message: "Unknown user: [$userObject]" ]
        }
    }

    @RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
    def updateUser(@PathVariable("id") int id, @RequestBody userObject) {

        if (usersStore.containsKey(id) && userObject instanceof Map) {

            def updated = usersStore.get(id)
            updated.each { key, value->
                if (userObject.containsKey(key)) {
                    updated[key] = userObject[key]
                }
            }

            [ success: true, message: "User updated", data: updated ]
        } else {
            [ success: false, message: "Unknown user id: [$id]" ]
        }
    }

}
