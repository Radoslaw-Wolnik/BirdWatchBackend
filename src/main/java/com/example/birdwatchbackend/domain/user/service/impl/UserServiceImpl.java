package com.example.birdwatchbackend.domain.user.service.impl;

import com.example.birdwatchbackend.domain.user.User;
import com.example.birdwatchbackend.domain.user.dto.UpdateProfileRequest;
import com.example.birdwatchbackend.domain.user.dto.UserProfileResponse;
import com.example.birdwatchbackend.domain.user.mapper.UserMapper;
import com.example.birdwatchbackend.domain.user.repository.UserRepository;
import com.example.birdwatchbackend.domain.user.service.UserService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toProfileResponse(user);
    }

    @Transactional
    @Override
    public UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userMapper.updateUserFromRequest(request, user);

        if (request.latitude() != null && request.longitude() != null) {
            Point point = geometryFactory.createPoint(
                    new Coordinate(request.longitude(), request.latitude()));
            user.setHomeLocation(point);
        }
        // If only label is provided without coordinates, keep old location.

        user = userRepository.save(user);
        return userMapper.toProfileResponse(user);
    }
}
