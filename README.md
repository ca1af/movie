# 개요


Swagger 사용한 API 명세
Application run 후 아래 링크
<br>
http://localhost:8080/swagger-ui/index.html

1. 추가 기능(페이징, 논리삭제, 동적조회)
2. 테스트 코드 작성

부분이 있습니다.

---

# 환경 설정

- H2 의 Local DB 활용했습니다.
- 부트시 DummyDataLoader 클래스를 사용해 20개의 더미데이터와 80개의 상응하는 이미지, 비디오 url 이 저장됩니다
- Java 17, SpringBoot 3.0.5 사용했으며 build.gradle 파일에 저장되어 별도의 설정이 필요 없습니다. 다만 SDK(17)의 경우 프로젝트 빌드를 위해 설치되어야 합니다.


---
# 테이블과 연관관계에 대해서

- Movie 테이블에 연결되어 있는 테이블은 MovieImage, MovieVideo, CastMember 테이블이 있습니다.
- Movie 엔티티의 필드에 넣지 않고 연관관계를 설정하여 다른 테이블로 빼놓은 이유는 데이터 베이스의 정규화를 위함입니다.
- 객체의 관점에서 보더라도, 이미지나 비디오는 확장의 가능성이 있으므로 다른 테이블에 놓는 것이 유리하다고 판단했습니다.

---
# 아키텍쳐

- Spring 프로젝트에서 Typical 하게 사용되는 레이어드 아키텍쳐를 사용했습니다.
- Presentation Layer 는 Controller를 사용해서 구현되었고, Business Layer는 Service(+Entity), Data Area는 JPA에서 제공하는 Repository와 Jdbc Template을 사용했습니다.

---


# MovieController (기본 기능) API  

### HTTP Status code 200번대 이외 결과들은 오류메시지와 함께 반환됨을 전제했습니다.

## Q1. GET /api/v1/movies

● 어떤 Query가 가능할까요?

: SELECT * FROM MOVIES M 등의 쿼리가 가능합니다.

- MOVIES 와 연결된 객체들(테이블) 인 MOVIE IMAGE, MOVIE VIDEO도 하나의 쿼리로 함께 조회되도록 API를 설계했습니다.
  - 예상 쿼리 :
    SELECT * FROM movie m
    LEFT JOIN movie_image mi
    ON MI.MOVIE_ID = M.MOVIE_ID

    LEFT JOIN movie_video mv

    ON m.id = mv.movie_id
    LEFT JOIN cast_member cm
    ON m.id = cm.movie_id
    WHERE INUSE = TRUE (논리 삭제를 위해 검색 조건 설정했습니다.)

- 추가기능으로 페이징 조회 쿼리가 구현되어 있습니다.
  - 예상 쿼리 :

    SELECT *
    FROM movie m
    LEFT JOIN movie_image mi ON m.id = mi.movie_id
    LEFT JOIN movie_video mv ON m.id = mv.movie_id
    LEFT JOIN cast_member cm ON m.id = cm.movie_id
    WHERE m.in_use = true
    OFFSET 0
    LIMIT 10


● 성공적인 경우, 어떤 https status code와 결과를 되돌려 줘야 할까요?

  : 200 CODE와 호응하는 JSON 데이터를 돌려줘야 합니다.

- JSON DATA 예시
  
````

    {
        "id": 1,
        "releaseDate": 1,
        "movieName": "Movie 1",
        "genre": "Genre 1",
        "director": "Director 1",
        "postImageUrl": "poster_1",
        "movieImages": [
            {
                "id": 1,
                "imageUrl": "image_1_for_movie_1",
                "movieId": 1
            },
            {
                "id": 2,
                "imageUrl": "image_2_for_movie_1",
                "movieId": 1
            },
            {
                "id": 3,
                "imageUrl": "image_3_for_movie_1",
                "movieId": 1
            },
            {
                "id": 4,
                "imageUrl": "image_4_for_movie_1",
                "movieId": 1
            },
            {
                "id": 5,
                "imageUrl": "image_5_for_movie_1",
                "movieId": 1
            }
        ],
        "movieVideos": [
            {
                "id": 1,
                "videoUrl": "video_1_for_movie_1",
                "movieId": 1
            },
            {
                "id": 2,
                "videoUrl": "video_2_for_movie_1",
                "movieId": 1
            },
            {
                "id": 3,
                "videoUrl": "video_3_for_movie_1",
                "movieId": 1
            }
        ],
        "castMembers": [
            {
                "id": 1,
                "movieId": 1,
                "memberName": "Cast Member 1"
            },
            {
                "id": 2,
                "movieId": 1,
                "memberName": "Cast Member 2"
            },
            {
                "id": 3,
                "movieId": 1,
                "memberName": "Cast Member 3"
            }
        ]
    },
````

● 만약 Query를 던졌는데 내용이 없다면 어떤 https status code와 결과를 되돌려 줘야
할까요?

  : 204 Status Code를 리턴해야 합니다. (No Content)

## A1. 영화 목록 조회

> 전체 영화 목록을 조회합니다. 연관된 영화 이미지나 비디오도 전부 하나의 쿼리로 조회됩니다.

- API 경로: `http://localhost:8080/api/v1/movies`

### 쿼리 예시

> Hibernate:
select
m1_0.id,
c1_0.movie_id,
c1_0.id,
c1_0.member_name,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m2_0.movie_id,
m2_0.id,
m2_0.image_url,
m1_0.movie_name,
m3_0.movie_id,
m3_0.id,
m3_0.video_url,
m1_0.original_title,
m1_0.poster_image_url,
m1_0.release_date,
m1_0.running_time,
m1_0.synopsis
from
movie m1_0
left join
movie_image m2_0
on m1_0.id=m2_0.movie_id
left join
movie_video m3_0
on m1_0.id=m3_0.movie_id
left join
cast_member c1_0
on m1_0.id=c1_0.movie_id
where
m1_0.in_use=?

## Q2. GET /api/v1/movies/{movie_id}

● 성공적인 경우, 어떤 https status code와 결과를 되돌려 줘야 할까요?
: 200 code 와 id 에 해당하는 Movie 객체의 정보를 Json 형식으로 돌려줘야 합니다.

- Json 데이터 예시

````
{
    "id": 1,
    "releaseDate": 1,
    "movieName": "Movie 1",
    "genre": "Genre 1",
    "director": "Director 1",
    "postImageUrl": "poster_1",
    "movieImages": [
        {
            "id": 1,
            "imageUrl": "image_1_for_movie_1",
            "movieId": 1
        },
        {
            "id": 2,
            "imageUrl": "image_2_for_movie_1",
            "movieId": 1
        },
        {
            "id": 3,
            "imageUrl": "image_3_for_movie_1",
            "movieId": 1
        },
        {
            "id": 4,
            "imageUrl": "image_4_for_movie_1",
            "movieId": 1
        },
        {
            "id": 5,
            "imageUrl": "image_5_for_movie_1",
            "movieId": 1
        }
    ],
    "movieVideos": [
        {
            "id": 1,
            "videoUrl": "video_1_for_movie_1",
            "movieId": 1
        },
        {
            "id": 2,
            "videoUrl": "video_2_for_movie_1",
            "movieId": 1
        },
        {
            "id": 3,
            "videoUrl": "video_3_for_movie_1",
            "movieId": 1
        }
    ],
    "castMembers": [
        {
            "id": 1,
            "movieId": 1,
            "memberName": "Cast Member 1"
        },
        {
            "id": 2,
            "movieId": 1,
            "memberName": "Cast Member 2"
        },
        {
            "id": 3,
            "movieId": 1,
            "memberName": "Cast Member 3"
        }
    ]
}
````

● 만약 해당하는 Entity가 없다면 어떤 https status code와 결과를 되돌려 줘야 할까요?
  : 404 NOT Found를 리턴해야 합니다.

## A2. 영화 상세 조회

> 하나의 영화를 조회합니다. 연관된 이미지나 비디오도 하나의 쿼리로 조회됩니다.

- API 경로: `http://localhost:8080/api/v1/movies/{movieId}`
- HTTP 메서드: GET

### 쿼리 예시

> Hibernate:
select
m1_0.id,
c1_0.movie_id,
c1_0.id,
c1_0.member_name,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m3_0.movie_id,
m3_0.id,
m3_0.image_url,
m1_0.movie_name,
m2_0.movie_id,
m2_0.id,
m2_0.video_url,
m1_0.original_title,
m1_0.poster_image_url,
m1_0.release_date,
m1_0.running_time,
m1_0.synopsis
from
movie m1_0
left join
movie_video m2_0
on m1_0.id=m2_0.movie_id
left join
movie_image m3_0
on m1_0.id=m3_0.movie_id
left join
cast_member c1_0
on m1_0.id=c1_0.movie_id
where
m1_0.in_use=?
and m1_0.id=?


## Q3. POST /api/v1/movies

● Input은 어떻게 줘야 할까요?
  : Json Body 이용해서 요청합니다.

````

POST /api/v1/movies
{
  "releaseDate": 1234567890,
  "movieName": "영화 제목",
  "genre": "장르",
  "director": "감독",
  "postImageUrl": "포스터 이미지 URL",
  "originalTitle": "원제",
  "runningTime": 120,
  "synopsis": "시놉시스"
}

````

● 성공적으로 Entity를 만들었다면 , 어떤 https status code와 결과를 되돌려 줘야
할까요?
  : 201 Created 와 함께 생성 URI 를 반환합니다.

● 만약 새롭게 생성한 Entity 가 기존에 있는 것과 충돌한다면, 어떤 https status code와
결과를 되돌려 줘야 할까요?
  : 409 Conflict 를 리턴합니다.

● 만약 해당하는 Entity를 생성하다가 다른 내부 문제가 생겼다면 어떤 https status code와 결과를 되돌려 줘야 할까요?
  : 400 BAD Request 리턴합니다. 잡지 못한 내부 문제의 경우 500을 리턴하지만, 최대한 문제되는 케이스를 찾아서 400을 리턴해야 합니다.

## A3. 영화 생성

> 영화를 생성합니다. 먼저 중복 체크를 위해 exist 쿼리를 날린 후 처리합니다.

- API 경로: `http://localhost:8080/api/v1/movies`

### 쿼리 예시
> select
1
from
movie m1_0
where
m1_0.movie_name=?


> Hibernate:
insert
into
movie
(id, director, genre, in_use, movie_name, original_title, poster_image_url, release_date, running_time, synopsis)
values
(default, ?, ?, ?, ?, ?, ?, ?, ?, ?)


- API 경로: `http://localhost:8080/api/v1/movies`
- HTTP 메서드: POST
- 요청 예시:

````
POST /api/v1/movies
{
  "releaseDate": 1234567890,
  "movieName": "영화 제목125125",
  "genre": "장르",
  "director": "감독",
  "postImageUrl": "포스터 이미지 URL",
  "originalTitle": "원제",
  "runningTime": 120,
  "synopsis": "시놉시스"
}


````

## Q4. PUT /api/v1/movies/{movie_id}

● 성공적으로 Entity를 변경했다면, 어떤 https status code와 결과를 되돌려 줘야
할까요?
  : 200 OK를 리턴합니다.

● 만약 변경한 Entity의 내용이 받아들일 수 없는 내용이라면, 어떤 https status code와
결과를 되돌려 줘야 할까요?
  : 400 BAD Request를 리턴합니다.

## A4. 영화 수정

> 영화의 세부 정보를 수정합니다. SELECT 쿼리로 찾아온 후 UPDATE 쿼리가 발생합니다.

- API 경로: `http://localhost:8080/api/v1/movies/{movieId}`

### 쿼리 예시
> Hibernate:
select
m1_0.id,
c1_0.movie_id,
c1_0.id,
c1_0.member_name,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m3_0.movie_id,
m3_0.id,
m3_0.image_url,
m1_0.movie_name,
m2_0.movie_id,
m2_0.id,
m2_0.video_url,
m1_0.original_title,
m1_0.poster_image_url,
m1_0.release_date,
m1_0.running_time,
m1_0.synopsis
from
movie m1_0
left join
movie_video m2_0
on m1_0.id=m2_0.movie_id
left join
movie_image m3_0
on m1_0.id=m3_0.movie_id
left join
cast_member c1_0
on m1_0.id=c1_0.movie_id
where
m1_0.in_use=?
and m1_0.id=?


> Hibernate:
update
movie
set
director=?,
genre=?,
in_use=?,
movie_name=?,
original_title=?,
poster_image_url=?,
release_date=?,
running_time=?,
synopsis=?
where
id=?


- 요청 예시:

````
{
  "releaseDate": 1234567890,
  "movieName": "수정 제목",
  "genre": "수정 장르",
  "director": "수정 감독",
  "postImageUrl": "수정 포스터 이미지 URL",
  "originalTitle": "수정 원제",
  "runningTime": 120,
  "synopsis": "수정 시놉시스"
}

````

## Q5. DELETE /api/v1/movies/{movie_id}

● 성공적으로 Entity를 삭제했다면, 어떤 https status code와 결과를 되돌려 줘야
할까요?
  : 204 NO Content 를 리턴합니다.

● 만약 삭제하려는 Entity가 없는 Entity라면, 어떤 https status code와 결과를 되돌려
줘야 할까요?
  : 404 NOT Found 를 리턴합니다.

## A5. 영화 삭제

> 영화를 삭제합니다. 연관된 엔티티들(이미지, 비디오) 도 모두 한 쿼리로 삭제됩니다.
> 해당 영화 조회용 쿼리 1개 + 삭제용 쿼리 3개가 발생합니다. (영화, 영화이미지, 영화비디오, 캐스트멤버(출연진))

- API 경로: `http://localhost:8080/api/v1/movies/{movieId}`

### 쿼리 예시

> Hibernate:
select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m1_0.movie_name,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
where
m1_0.id=?

> Hibernate:
delete
from
movie_video
where
movie_id=movie_id


> Hibernate:
delete
from
movie_image
where
movie_id=movie_id

> Hibernate:
delete
from
cast_member
where
movie_id=movie_id


> Hibernate:
delete
from
movie
where
id=?


---

# MyMovieController (추가 기능들) API 목록

## 1. 페이징을 통한 영화 조회

> 페이징을 통해서 10개씩 영화를 조회합니다.
> 해당하는 페이지의 인덱스가 있는지 확인하고, 인덱스가 하나도 존재하지 않는다면 리턴하는 배열을 새 배열로 초기화해서 204 No content 를 리턴합니다.

### 쿼리 예시

> Hibernate:
select
1
from
movie m1_0
where
m1_0.id between ? and ?


> Hibernate:
select
m1_0.id,
c1_0.movie_id,
c1_0.id,
c1_0.member_name,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m2_0.movie_id,
m2_0.id,
m2_0.image_url,
m1_0.movie_name,
m3_0.movie_id,
m3_0.id,
m3_0.video_url,
m1_0.original_title,
m1_0.poster_image_url,
m1_0.release_date,
m1_0.running_time,
m1_0.synopsis
from
movie m1_0
left join
movie_image m2_0
on m1_0.id=m2_0.movie_id
left join
movie_video m3_0
on m1_0.id=m3_0.movie_id
left join
cast_member c1_0
on m1_0.id=c1_0.movie_id
where
m1_0.in_use=?


- API 경로 : 'http://localhost:8080/api/v1/movies/pages/{pageNum}'
- HTTP 매서드 : GET
- 응답 예시(Json Body)

````
http://localhost:8080/api/v1/movies/pages/3 으로 요청
-> 21~30 사이 Id 가진 엔티티 조회

[
    {
        "id": 21,
        "releaseDate": 1234567890,
        "movieName": "영화 제목1125",
        "genre": "장르",
        "director": "감독",
        "postImageUrl": "포스터 이미지 URL",
        "movieImages": [],
        "movieVideos": [],
        "castMembers": []
    },
    {
        "id": 22,
        "releaseDate": 1234567890,
        "movieName": "영화 제목112",
        "genre": "장르",
        "director": "감독",
        "postImageUrl": "포스터 이미지 URL",
        "movieImages": [],
        "movieVideos": [],
        "castMembers": []
    }
]
]

````

- 응답:
  - 성공(200 OK): 영화 목록 조회 결과를 담은 MovieResponseDto 리스트
  - 실패(204 No Content): 조회된 영화 목록이 없는 경우

# 2. 동적 조회 조건을 사용한 영화 조회

> 동적 조회 조건(영화 이름, 감독 이름) 을 활용한 조회입니다

### 쿼리 예시

> m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m2_0.movie_id,
m2_0.id,
m2_0.image_url,
m1_0.movie_name,
m3_0.movie_id,
m3_0.id,
m3_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_image m2_0
on m1_0.id=m2_0.movie_id
left join
movie_video m3_0
on m1_0.id=m3_0.movie_id
left join
cast_member c1_0
on m1_0.id=c1_0.movie_id
where
m1_0.movie_name like ? escape '!'
and m1_0.in_use=?


- API 경로 : 'http://localhost:8080/api/v1/movies/search'
- HTTP 매서드 : GET
- 요청 예시
````
1. 영화 제목으로만 조회
{
  "movieName": "Movie"
}

2. 감독으로만 조회
{
  "director": "Director"
}

3. 두 조건에 모두 맞는 데이터 조회
{
  "movieName": "Movie",
  "director": "Director"
}

````
- 응답:
  - 성공(200 OK): 영화 목록 조회 결과를 담은 MovieResponseDto 리스트
  - 실패(400 BAD REQUEST): RequestBody 형태의 Json 데이터가 올바르지 않은 경우
  - 실패(204 No Content): 조회된 영화 목록이 없는 경우
  
# 3. 논리 삭제

> Movie 엔티티에 설정한 inUse 테이블을 이용해서, 논리 삭제합니다.

> 조회 후 UPDATE 쿼리가 발생합니다.

### 쿼리 예시

> Hibernate:
select
m1_0.id,
m1_0.director,
m1_0.genre,
m1_0.in_use,
m3_0.movie_id,
m3_0.id,
m3_0.image_url,
m1_0.movie_name,
m2_0.movie_id,
m2_0.id,
m2_0.video_url,
m1_0.poster_image_url,
m1_0.release_date
from
movie m1_0
left join
movie_video m2_0
on m1_0.id=m2_0.movie_id
left join
movie_image m3_0
on m1_0.id=m3_0.movie_id
left join
cast_member c1_0
on m1_0.id=c1_0.movie_id
where
m1_0.in_use=?
and m1_0.id=?

>Hibernate:
update
movie
set
director=?,
genre=?,
in_use=?,
movie_name=?,
poster_image_url=?,
release_date=?
where
id=?


- API 경로 : 'http://localhost:8080/api/v1/movies/{movieId}'
- HTTP 매서드 : Patch
- 응답:
  - 성공(200 OK): 영화 삭제 성공
  - 실패(404 Not Found): 해당하는 영화가 없는 경우
