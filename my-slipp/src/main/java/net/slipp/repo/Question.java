package net.slipp.repo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Question extends AbstractEntity{
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)  //이 설정을 안해주면 테이블마다 유일하게 생성되어야할 id값이 서로 연관돼서 생성된다..!
//	@Column
//	@JsonProperty
//	private Long id;
	
	//private String writer;
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	@JsonProperty
	private User writer;
	
	@OneToMany(mappedBy="question") //answer에서 @ManyToOne 의 필드이름으로 맵핑 시켜주면 된다.
	@OrderBy("id DESC")
	private List<Answer> answers;  //여기에 있는 이름 그대로 mustache에서 보여줄 수 있다.
	
	@JsonProperty
	private String title;
	
	@Lob
	@JsonProperty
	private String contents;
	
	@JsonProperty
	private Integer countOfAnswer = 0;
	
//	@CreationTimestamp    //이걸 해주면 밑에 createDate에 값넣는 게 필요가 없어진다.
//	private LocalDateTime createDate;
	
	public Question() {}
	
	public Question(User writer, String title, String contents) {
		super();
		this.writer = writer;
		this.title = title;
		this.contents = contents;
//		this.createDate = LocalDateTime.now();
	}
	
//	public String getFormattedCreateDate() {
//		if (createDate == null) {
//			return "";
//		}
//		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
//	}

	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;		
	}
	
	public void addAnswer() {
		this.countOfAnswer += 1;
	}
	
	public void deleteAnswer() {
		this.countOfAnswer -= 1;
	}

	public boolean isSameWriter(User loginUser) {
		return this.writer.equals(loginUser); //자바에서 equals라는 함수는 다른 인스턴스라도 같은 값을 가리키고 있으면 true를 리턴하는 
											//함수인데 비교를 할 기준이 있어야만 이 함수를 제대로 실행할 수 있다.
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Question other = (Question) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}

	
	
	
}
