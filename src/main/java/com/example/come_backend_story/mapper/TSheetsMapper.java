package com.example.come_backend_story.mapper;

import com.example.come_backend_story.dto.GroupDTO;
import com.example.come_backend_story.dto.JobCodeDTO;
import com.example.come_backend_story.dto.UserDTO;
import com.example.come_backend_story.entity.GroupEntity;
import com.example.come_backend_story.entity.JobCodeEntity;
import com.example.come_backend_story.entity.UserEntity;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;

@Component
public class TSheetsMapper {

  /**
   * Maps TSheets JobCode DTO to Entity
   */
  public JobCodeEntity toJobCodeEntity(JobCodeDTO dto) {
    if (dto == null) {
      return null;
    }

    return JobCodeEntity.builder()
        .id(dto.getId())
        .name(dto.getName())
        .active(dto.getActive() != null ? dto.getActive() : true)
        .type(dto.getType())
        .billable(dto.getBillable())
        .assignedToAll(dto.getAssignedToAll())
        .parentId(dto.getParentId())
        .shortCode(dto.getShortCode())
        .lastModified(dto.getLastModified() != null ? dto.getLastModified() : OffsetDateTime.now())
        .syncedAt(OffsetDateTime.now())
        .deleted(false)
        .build();
  }


  public GroupEntity toGroupEntity(GroupDTO dto) {
    if (dto == null) return null;

    return GroupEntity.builder()
        .id(dto.getId())
        .name(dto.getName())
        .active(dto.getActive() != null ? dto.getActive() : true)
        .lastModified(dto.getLastModified() != null ? dto.getLastModified() : OffsetDateTime.now())
        .syncedAt(OffsetDateTime.now())
        .deleted(false)
        .build();
  }

  public UserEntity toUserEntity(UserDTO dto) {
    if (dto == null) return null;

    return UserEntity.builder()
        .id(dto.getId())
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .groupId(dto.getGroupId())
        .email(dto.getEmail())
        .cellNumber(dto.getCellNumber())
        .active(dto.getActive() != null ? dto.getActive() : true)
        .lastModified(dto.getLastModified() != null ? dto.getLastModified() : OffsetDateTime.now())
        .syncedAt(OffsetDateTime.now())
        .deleted(false)
        .build();
  }
}