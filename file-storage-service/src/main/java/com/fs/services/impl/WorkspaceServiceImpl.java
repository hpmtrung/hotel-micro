package com.fs.services.impl;

import com.fs.domain.Client;
import com.fs.domain.Workspace;
import com.fs.model.WorkspaceCreate;
import com.fs.model.WorkspaceDTO;
import com.fs.repositories.WorkspaceRepository;
import com.fs.services.WorkspaceService;
import com.fs.services.exceptions.WorkspaceNameUniqueException;
import com.fs.services.exceptions.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

  private final WorkspaceRepository workspaceRepository;

  @Override
  public WorkspaceDTO createWorkspace(WorkspaceCreate data, Client client) {
    if (workspaceRepository.existsByNameAndOwnerId(data.getName(), client.getId())) {
      throw new WorkspaceNameUniqueException();
    }
    Workspace workspace = new Workspace();
    workspace.setName(data.getName());

    client.addWorkspace(workspace);

    workspace = workspaceRepository.save(workspace);
    return new WorkspaceDTO(workspace.getId(), workspace.getName());
  }

  @Override
  @Transactional(readOnly = true)
  public Workspace getWorkspace(int workspaceId) {
    return workspaceRepository.findById(workspaceId).orElseThrow(WorkspaceNotFoundException::new);
  }

  @Override
  @Transactional(readOnly = true)
  public Workspace getWorkspace(String name, Client client) {
    return workspaceRepository
        .findByNameAndOwnerId(name, client.getId())
        .orElseThrow(WorkspaceNotFoundException::new);
  }
}