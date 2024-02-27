package Test;
import java.beans.Transient;

import javax.print.attribute.standard.Media;

import org.junit.runner.Runwith;
import org.springframework.test.web.servlet.MoclMvc;
import org.mocking.junit.MockitoJunitRunner;

@Runwith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookRespository bookRespository;

    @InjectMocks
    private BookController bookController;

    Book BookRECORD_1 = new Book(1L,"Heelo World"," How to build World",5);
    Book BookRECORD_2 = new Book(2L,"Heelo World"," How to build World",5);
    Book BookRECORD_3 = new Book(3L,"Heelo World"," How to build World",5);

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllRecords_success() throws Exception {
        List<Book> records = new ArrayList<>(Arrays.asList(BookRECORD_1,BookRECORD_2,BookRECORD_3));
        Mockito.when(bookRespository.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders
         .get('/book')
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$",hashSize(3)))
         .andExpect(jsonPath(expression:"$[2].name",is(value:"Grokking Algorithms")));
        
    }
    @Test
    public void getBookById_success() throws Exception{
        Mockito.when(bookRespository.findById(RECORD_1.getBookID())).thenReturn(java.util.Optionalof(RECORD_1));
        Mockito.when(bookRespository.findAll()).thenReturn(records);
        mockMvc.perform(MockMvcRequestBuilders
         .get('/book')
         .contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$",notNullValue()))
         .andExpect(jsonPath(expression:"$[2].name",is(value:"Grokking Algorithms")));
    }
    @Test
    public void createRecord_success() throws Exception{
        Book record =Book.builder()
           .bookId(4L)
           .name("Introduction to C")
           .summary("The name but longer")
           .rating(5)
           .build();

           Mockito.when(BookRespository.save(record)).thenReturn(record);
           String content = objectWriter.writeValueAsString(record);

           MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilder.post("/book")
           .contentType(MediaType.APPLICATION_JSON)
           .accept(MediaType.APPLICATION_JSON)
           .content(content)

           mockMvc.perform(mockRequest)
           .andExpect(status().isOk())
           .andExpect(jsonPath(expression: "$",notNullValue()))
           .andExpect(jsonPath(expression:'$.name',is(value:"Introduction to c")));
    }
    @Test
    public void updateBookRecord_success() throws Exception{
        Book updatedRecord=Book.builder()
        .bookId(1L);
        .name("update Book Name")
        .summary("update summary")
        .rating(1)
        .build();
        Mockito.when(bookRespository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.ofNullable(RECORD_1));
        Mockito.when(bookRespository.save(updatedRecord)).thenReturn(updatedRecord);
 
         String updatedContent =objectWriter.writeValueAsString(updatedRecord);
         MockHttpServletRequestBuilder mockRequest =MockMvcRequestBuilder.put('/book')
         .contentType(MediaType.APPLICATION_JSON)
         .accept(MediaType.APPLICATION_JSON)
         .content(updatedContent);

         mockMvc.perform(mockRequest)
         .andExpect(status().isOk())
         .andExpect(jsonPath(expression:'$',notNullValue))
         .andExcept(jsonPath('$.name',is("updated Book Name")));


    }
    @Test
    public void deleteBookByID_success()throws Exception{
        Mockito.when(bookRespository.findById(RECORD_2.getBookId())).thenReturn(Optional.of(RECORD_2));

        mockMvc.perform(MockMvcRequestBuilders
        .delete("/book/2")
        .contentType(MedialType.APPLICATION_JSON)
        .andExceptstatus().isOk());
    }
}
