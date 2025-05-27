# Player Management API Test Cases

## Supervisor Role Tests
1. TC-R-01: Supervisor creates user with USER role
   - Verify successful user creation with USER role by supervisor
   - Check response status and data correctness

2. TC-R-02: Supervisor creates user with ADMIN role
   - Verify successful admin creation by supervisor
   - Check response status and data correctness

3. TC-R-06: Supervisor attempts to delete supervisor user (negative)
   - Verify supervisor cannot delete another supervisor
   - Check forbidden status and error message

4. TC-R-12: Get all players
   - Verify successful retrieval of all players
   - Check response status and data presence

5. TC-R-13: Get player by ID
   - Verify successful retrieval of specific player
   - Check response status and data correctness

6. TC-R-14: Get player by non-existent ID (negative)
   - Verify proper handling of non-existent player request
   - Check not found status and error message

7. TC-R-15: Supervisor updates USER
   - Verify successful user update by supervisor
   - Check response status and updated data

## Admin Role Tests
1. TC-R-03: Admin creates user with USER role
   - Verify successful user creation by admin
   - Check response status and data correctness

2. TC-R-04: Admin attempts to create user with SUPERVISOR role
   - Verify admin cannot create supervisor
   - Check forbidden status and error message

3. TC-R-16: Admin updates USER
   - Verify successful user update by admin
   - Check response status and updated data

## User Role Tests
1. TC-R-11: User cannot create another user
   - Verify user cannot create new users
   - Check forbidden status and error message

2. TC-R-12: User updates their own data
   - Verify successful self-update by user
   - Check response status and updated data

## Player Delete Tests
1. TC-R-08: Supervisor deletes ADMIN
   - Verify successful admin deletion by supervisor
   - Check response status

2. TC-R-09: Admin deletes USER
   - Verify successful user deletion by admin
   - Check response status

3. TC-R-10: Admin tries to delete ADMIN
   - Verify admin cannot delete other admins
   - Check forbidden status and error message

4. TC-R-13: User tries to delete any user
   - Verify user cannot delete players
   - Check forbidden status and error message 