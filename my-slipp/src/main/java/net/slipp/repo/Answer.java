package net.slipp.repo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Answer extends AbstractEntity{

//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@JsonProperty
//	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
	@JsonProperty
	private User writer;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	@JsonProperty
	private Question question;
	
	@Lob
	@JsonProperty
	private String contents;
	
//	@CreationTimestamp
//	@JsonProperty
//	private LocalDateTime createDate;
	
	public Answer() {}
	
	public Answer(User writer,Question question, String contents) {
		this.writer = writer;
		this.contents = contents;
		this.question = question;
	}
	
	public boolean isSameWriter(User loginUser) {
		return loginUser.equals(this.writer);
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
//		Answer other = (Answer) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return "Answer [=" + super.toString() + ", writer=" + writer + ", contents=" + contents + "]";
	}

	
	
	
}
	
