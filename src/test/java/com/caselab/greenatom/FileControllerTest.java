package com.caselab.greenatom;

import com.caselab.greenatom.controller.FileController;
import com.caselab.greenatom.repository.FileRepository;
import com.caselab.greenatom.dto.File;
import com.caselab.greenatom.response.FileResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.mockito.Mockito.*;

@Testcontainers
@ExtendWith(SpringExtension.class)
@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileRepository fileRepository;

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    static {
        postgreSQLContainer.start();
    }

    @Test
    void testGetAllFiles() throws Exception {
        when(fileRepository.findAll()).thenReturn(List.of(new File()));

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAddFile() throws Exception {
        File file = File.builder()
                .fileId(1L)
                .title("New File 1")
                .description("File description")
                .file("3245GERWcweRE/")
                .build();

        when(fileRepository.existsByTitle(file.getTitle())).thenReturn(false);
        when(fileRepository.existsByFile(file.getFile())).thenReturn(false);
        when(fileRepository.save(any(File.class))).thenReturn(file);

        String fileJson = "{\"title\":\"New File 1\",\"description\":\"File description\",\"file\":\"3245GERWcweRE/\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fileJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileId").value(file.getFileId()));
    }

    @Test
    void testGetById_Found() throws Exception {
        Long fileId = 1L;
        File file = File.builder()
                .fileId(fileId)
                .title("Existing File")
                .description("Description")
                .file("filecontent")
                .build();

        when(fileRepository.findById(fileId)).thenReturn(java.util.Optional.of(file));

        mockMvc.perform(MockMvcRequestBuilders.get("/" + fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileId").value(fileId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Existing File"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        Long fileId = 1L;

        when(fileRepository.findById(fileId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/" + fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteFileById_Found() throws Exception {
        Long fileId = 1L;

        when(fileRepository.existsById(fileId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/" + fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(fileRepository, times(1)).deleteById(fileId);
    }

    @Test
    void testDeleteFileById_NotFound() throws Exception {
        Long fileId = 1L;

        when(fileRepository.existsById(fileId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/" + fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(fileRepository, never()).deleteById(fileId);
    }
}
