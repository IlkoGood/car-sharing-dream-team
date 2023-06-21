package com.carsharing.service.impl;

import com.carsharing.exception.DataProcessingException;
import com.carsharing.model.User;
import com.carsharing.repository.UserRepository;
import com.carsharing.util.UtilForTests;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest extends UtilForTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void save_Ok() {
        User expected = getUser();
        Mockito.when(userRepository.save(expected)).thenReturn(expected);
        User actual = userService.save(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_Ok() {
        Long id = 1L;
        User expected = getUser();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(expected));
        User actual = userService.findById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_notFound_throwsException() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataProcessingException.class, () -> userService.findById(id));
    }

    @Test
    void findByEmail_Ok() {
        String email = "test@test.com";
        User expected = getUser();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(expected));
        User actual = userService.findByEmail(email);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByEmail_notFound_throwsException() {
        String email = "test@test.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataProcessingException.class, () -> userService.findByEmail(email));
    }
}
