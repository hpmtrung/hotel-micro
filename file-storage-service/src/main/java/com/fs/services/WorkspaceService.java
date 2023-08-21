package com.fs.services;

import com.fs.domain.Client;
import com.fs.domain.Workspace;
import com.fs.model.WorkspaceCreate;
import com.fs.model.WorkspaceDTO;

public interface WorkspaceService {

  WorkspaceDTO createWorkspace(WorkspaceCreate dto, Client client);

  Workspace getWorkspace(int workspaceId);

  Workspace getWorkspace(String name, Client client);
}