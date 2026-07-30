package com.quitto.server.application.services.Machine;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quitto.server.domain.Repository.Machine.MachineRepository;
import com.quitto.server.domain.Repository.users.UserRepository;
import com.quitto.server.domain.models.Machine.Machine;
import com.quitto.server.domain.models.User.User;
import com.quitto.server.domain.exception.MachineNotFoundException;

@Service
public class MachineService {

    private final MachineRepository repository;
    private final UserRepository userRepository;

    public MachineService(MachineRepository repository, UserRepository userRepository){
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void changeOwner(Long machineId, Long userId){
        Machine machine = repository.findById(machineId)
            .orElseThrow(() -> new MachineNotFoundException("Machine not registered" + machineId));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        machine.changeOwner(user);

        repository.save(machine);
    }

}
