package demo

import spock.lang.Specification

class ExtjsDataApplicationSpec extends Specification {

    ExtjsDataApplication uut = new ExtjsDataApplication()

    def "list books"() {
        given:
        def books = uut.books() 
        
        expect:
        books.success
        books.message == "Loaded data"
        books.data == [  "Zend Framework", "Beginning F#", "Pro Hadoop" ]
    }

    def "get users"() {
        given:
        def result = uut.getUsers() 
        
        expect:
        result.success
        result.message == "Loaded data"
        result.data.size() == 6
        result.data.collect { it.id } == 1..6
        result.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "get user"() {
        given:
        def result = uut.getUser(4)
        
        expect:
        result.success
        result.message == "Loaded data"
        result.data.id == 4
        result.data.first == "Barney"
    }

    def "get unknown user 0"() {
        given:
        def result = uut.getUser(0)
        
        expect:
        ! result.success
        result.message == "Unknown user: [0]"
        ! result.containsKey('data')
    }

    def "get unknown user 99"() {
        given:
        def result = uut.getUser(99)
        
        expect:
        ! result.success
        result.message == "Unknown user: [99]"
        ! result.containsKey('data')
    }

    def "delete user"() {
        given:
        def result = uut.deleteUser(4) 
        
        expect:
        result.success
        result.message == "User deleted"
        result.data.id == 4
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 5
        users.data.collect { it.id } == (1..3) + (5..6)
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Betty", "Bamm-Bamm"]
    }

    def "delete first user"() {
        given:
        def result = uut.deleteUser(1) 
        
        expect:
        result.success
        result.data.id == 1
        
        def users = uut.getUsers() 
        users.success
        users.data.size() == 5
        users.data.collect { it.id } == 2..6
        users.data.collect { it.first } == ["Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "delete last user"() {
        given:
        def result = uut.deleteUser(6) 
        
        expect:
        result.success
        result.data.id == 6
        
        def users = uut.getUsers() 
        users.success
        users.data.size() == 5
        users.data.collect { it.id } == 1..5
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty"]
    }

    def "delete unknown user 0"() {
        given:
        def result = uut.deleteUser(0) 
        
        expect:
        ! result.success
        result.message == "Unknown user: [0]"
        ! result.containsKey('data')
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "delete unknown user 99"() {
        given:
        def result = uut.deleteUser(99) 
        
        expect:
        ! result.success
        result.message == "Unknown user: [99]"
        ! result.containsKey('data')
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "add user"() {
        given:
        def user = [first: "Joe", last: "Rockhead", email: "joe@bedrock-vfd.org"]
        def result = uut.addUser(user) 
        
        expect:
        result.success
        result.message == "User added"
        result.data.id == 7
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 7
        users.data.collect { it.id } == 1..7
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm", "Joe"]
    }

    def "add user id 99"() {
        given:
        def user = [id: 99, first: "Joe", last: "Rockhead", email: "joe@bedrock-vfd.org"]
        def result = uut.addUser(user) 
        
        expect:
        result.success
        result.message == "User added"
        result.data.id == 7
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 7
        users.data.collect { it.id } == 1..7
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm", "Joe"]
    }

    def "add invalid user"() {
        given:
        def result = uut.addUser("Joe Rockhead") 
        
        expect:
        ! result.success
        result.message == "Invalid user object: [Joe Rockhead]"
        ! result.data
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "add user no first name"() {
        given:
        def user = [last: "Rockhead", email: "joe@bedrock-vfd.org"]
        def result = uut.addUser(user) 
        
        expect:
        ! result.success
        result.message == "Invalid user object - missing elements: [[last:Rockhead, email:joe@bedrock-vfd.org]]"
        ! result.data
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "update user"() {
        given:
        def user = [id: 5, first: "Elizabeth"]
        def result = uut.updateUser(user) 
        
        expect:
        result.success
        result.message == "User updated"
        result.data.id == 5
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Elizabeth", "Bamm-Bamm"]
    }
    
    def "update invalid user"() {
        given:
        def result = uut.updateUser("Joe Rockhead")
        
        expect:
        ! result.success
        result.message == "Invalid user object: [Joe Rockhead]"
        ! result.data
        
        def users = uut.getUsers()
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }
    
    def "update unknown user"() {
        given:
        def user = [id: 99, first: "Joe", last:""]
        def result = uut.updateUser(user)
        
        expect:
        ! result.success
        result.message == "Unknown user id: [99]"
        ! result.data
        
        def users = uut.getUsers()
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }

    def "update user 5"() {
        given:
        def user = [first: "Elizabeth"]
        def result = uut.updateUser(5, user) 
        
        expect:
        result.success
        result.message == "User updated"
        result.data.id == 5
        
        def users = uut.getUsers() 
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Elizabeth", "Bamm-Bamm"]
    }
    
    def "update invalid user 99"() {
        given:
        def user = [id: 99, first: "Joe", last: "Rockhead", email: "joe@bedrock-vfd.org"]
        def result = uut.updateUser(99, user)
        
        expect:
        ! result.success
        result.message == "Unknown user id: [99]"
        ! result.data
        
        def users = uut.getUsers()
        users.success
        users.message == "Loaded data"
        users.data.size() == 6
        users.data.collect { it.id } == 1..6
        users.data.collect { it.first } == ["Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm-Bamm"]
    }
}
