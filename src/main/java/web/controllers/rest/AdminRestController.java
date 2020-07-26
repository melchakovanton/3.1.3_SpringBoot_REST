package web.controllers.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.UserDto;
import web.service.ServiceAbstractInterface;

@RestController
@RequestMapping("/rest")
public class AdminRestController {

    private final ServiceAbstractInterface<UserDto> serviceAbstractInterface;

    public AdminRestController(ServiceAbstractInterface<UserDto> serviceAbstractInterface) {
        this.serviceAbstractInterface = serviceAbstractInterface;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsersRest() {
        return new ResponseEntity<>(serviceAbstractInterface.allEntity(), HttpStatus.OK);
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getUser(@PathVariable("user-id") Long id) {
        return new ResponseEntity<>(serviceAbstractInterface.getEntityById(id), HttpStatus.OK);
    }

    @PostMapping("/userAdd")
    public ResponseEntity<?> addUserRest(@RequestBody UserDto userDto) {
        if (serviceAbstractInterface.addEntity(userDto)) {
            return ResponseEntity.ok(serviceAbstractInterface.getEntityByName(userDto.getName()));
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }

    @DeleteMapping("/delete/{user-id}")
    public ResponseEntity<?> deleteUserRest(@PathVariable("user-id") Long id) {
        serviceAbstractInterface.deleteEntity(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/editUser")
    public ResponseEntity<UserDto> editUser(@RequestBody UserDto userDto) {
        if (serviceAbstractInterface.updateEntity(userDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
